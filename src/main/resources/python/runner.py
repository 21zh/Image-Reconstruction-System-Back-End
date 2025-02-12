import numpy as np
import os
import cv2
import torch
import torch.backends.cudnn
import torch.utils.data
import sys

import utils.binvox_visualization
import utils.data_loaders
import utils.data_transforms
import utils.network_utils
import utils.binvox_rw

from config import cfg

from models.encoder import Encoder
from models.decoder import Decoder
from models.refiner import Refiner
from models.merger import Merger


def test_net(cfg,
             epoch_idx=-1,
             test_data_loader=None,
             encoder=None,
             decoder=None,
             refiner=None,
             merger=None):
    # Enable the inbuilt cudnn auto-tuner to find the best algorithm to use
    torch.backends.cudnn.benchmark = True

    # Set up data loader
    if test_data_loader is None:
        # Set up data augmentation
        IMG_SIZE = cfg.CONST.IMG_H, cfg.CONST.IMG_W
        CROP_SIZE = cfg.CONST.CROP_IMG_H, cfg.CONST.CROP_IMG_W
        test_transforms = utils.data_transforms.Compose([
            utils.data_transforms.CenterCrop(IMG_SIZE, CROP_SIZE),
            utils.data_transforms.RandomBackground(cfg.TEST.RANDOM_BG_COLOR_RANGE),
            utils.data_transforms.Normalize(mean=cfg.DATASET.MEAN, std=cfg.DATASET.STD),
            utils.data_transforms.ToTensor(),
        ])

        image_path = sys.argv[1]

    # Set up networks
    if decoder is None or encoder is None:
        encoder = Encoder(cfg)
        decoder = Decoder(cfg)
        refiner = Refiner(cfg)
        merger = Merger(cfg)

        if torch.cuda.is_available():
            encoder = torch.nn.DataParallel(encoder).cuda()
            decoder = torch.nn.DataParallel(decoder).cuda()
            refiner = torch.nn.DataParallel(refiner).cuda()
            merger = torch.nn.DataParallel(merger).cuda()

        checkpoint = torch.load('E:/server/resources/Pix2Vox-A-ShapeNet.pth', map_location=torch.device('cpu'))

        # 去掉`module.`前缀
        from collections import OrderedDict

        def remove_module_prefix(state_dict):
            new_state_dict = OrderedDict()
            for k, v in state_dict.items():
                name = k[7:]  # 去掉`module.`前缀
                new_state_dict[name] = v
            return new_state_dict

        # 加载新的state_dict
        encoder.load_state_dict(remove_module_prefix(checkpoint['encoder_state_dict']))
        decoder.load_state_dict(remove_module_prefix(checkpoint['decoder_state_dict']))

        if cfg.NETWORK.USE_REFINER:
            refiner.load_state_dict(remove_module_prefix(checkpoint['refiner_state_dict']))
        if cfg.NETWORK.USE_MERGER:
            merger.load_state_dict(remove_module_prefix(checkpoint['merger_state_dict']))

    # Switch models to evaluation mode
    encoder.eval()
    decoder.eval()
    refiner.eval()
    merger.eval()

    rendering_image_paths = [image_path]
    selected_rendering_image_paths = [rendering_image_paths[i] for i in range(cfg.CONST.N_VIEWS_RENDERING)]
    rendering_images = []
    for image_path in selected_rendering_image_paths:
        rendering_image = cv2.imread(image_path, cv2.IMREAD_UNCHANGED).astype(np.float32) / 255.
        rendering_images.append(rendering_image)
    rendering_images = np.asarray(rendering_images)
    rendering_images = test_transforms(rendering_images)
    rendering_images = rendering_images.unsqueeze(0)

    with torch.no_grad():
        # Get data from data loader
        rendering_images = utils.network_utils.var_or_cuda(rendering_images)

        # Test the encoder, decoder, refiner and merger
        image_features = encoder(rendering_images)
        raw_features, generated_volume = decoder(image_features)

        if cfg.NETWORK.USE_MERGER and epoch_idx >= cfg.TRAIN.EPOCH_START_USE_MERGER:
            generated_volume = merger(raw_features, generated_volume)
        else:
            generated_volume = torch.mean(generated_volume, dim=1)

        if cfg.NETWORK.USE_REFINER and epoch_idx >= cfg.TRAIN.EPOCH_START_USE_REFINER:
            generated_volume = refiner(generated_volume)

        # IoU per sample
        for th in cfg.TEST.VOXEL_THRESH:
            _volume = torch.ge(generated_volume, th).float()

        # 保存测试生成的3D模型
        binvox_file = sys.argv[2]
        if not os.path.exists(os.path.dirname(binvox_file)):
            os.makedirs(os.path.dirname(binvox_file))
        gv = generated_volume.cpu().numpy()
        gv = (gv.squeeze() > cfg.TEST.VOXEL_THRESH[0]).astype(np.int32)  # change numpy datatype to bool
        with open(binvox_file, 'wb') as f:
            voxel_model = utils.binvox_rw.Voxels(data=gv, dims=gv.shape, translate=[0, 0, 0], scale=1, axis_order='xyz')
            voxel_model.write(f)
        print("3D model saved successfully!")


if __name__ == '__main__':
    test_net(cfg)