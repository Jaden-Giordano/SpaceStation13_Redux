function onInteract(object, event)
	print("[Event]"..event.getInteracted().getName().." clicked by "..event.getInteracter().getName())
	object.getGuiHandler().createGui(200, 200)
end