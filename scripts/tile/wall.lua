function onInteract(object, event)
	interacter = event.getInteracter()
	interacted = event.getInteracted()
	interacted.setSolid(not interacted.isSolid())
end