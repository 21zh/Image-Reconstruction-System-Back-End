# -*- coding: utf-8 -*-
import cv2
import matplotlib
matplotlib.use('Agg')  # 使用非交互模式，避免图形界面弹出
import matplotlib.pyplot as plt
import os

from mpl_toolkits.mplot3d import Axes3D

def get_volume_views(volume, save_dir, filename, IoU, n_itr):
    # 如果保存目录不存在，则创建它
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)

    # 压缩维度并将体素阈值二值化
    volume = volume.squeeze().__ge__(0.5)

    # 创建绘图对象和3D子图
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')  # 替换 gca()

    # 设置绘图参数
    ax.set_aspect('equal')
    ax.voxels(volume, edgecolor="k")
    ax.view_init(elev=5, azim=45)  # 设置视角

    # 关闭坐标轴并保存图片
    plt.axis('off')
    save_path = os.path.join(save_dir, '%04f-%s.png' % (IoU, filename))
    plt.savefig(save_path, bbox_inches='tight')
    plt.close()

    # 读取并返回保存的图片
    return cv2.imread(save_path)