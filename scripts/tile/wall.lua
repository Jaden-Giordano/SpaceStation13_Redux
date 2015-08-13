function init(object)
	object.setName("Wall")
	object.attachImageFromSheet(2, "turf")
	object.setSolid(true)
end

function onInteract(object, event)
	interacter = event.getInteracter()
	interacted = event.getInteracted()
	interacted.setSolid(not interacted.isSolid())
end