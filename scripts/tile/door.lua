state = 0 --0 = open, 1 = closed

function init(object)
    object.setName("door")

    anim = object.attachAnimation("tiles/door.png", .06)
    anim.setZone(6, 0)
    anim.stop()
    anim.setMode("once")

    object.setSolid(true)
    state = 1
end

function onInteract(object, event)
    anim = object.getAnimation()
    if (anim ~= nil) then
        if (state == 0) then
            anim.setZone(0, 6)
            anim.play();
            state = 1
            object.setSolid(true)
        else
            anim.setZone(6, 0);
            anim.play();
            state = 0
            object.setSolid(false)
        end
    end
end

