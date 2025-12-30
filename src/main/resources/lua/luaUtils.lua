--用户是否已在房间
if redis.call('EXISTS', KEYS[2]) == 1 then
    --return -2  --重复加入
end

--获取房间容量
local count = tonumber(redis.call('GET', KEYS[1]))
if not count    then
    return -1
end

if count < tonumber(ARGV[1])    then
    return 0
end

--扣房间容量
redis.call('DECRBY', KEYS[1], ARGV[1])

--加用户锁
redis.call('SET', KEYS[2], 1)
redis.call('EXPIRE', KEYS[2], tonumber(ARGV[2]))

return 1