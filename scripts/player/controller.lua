down = false
up = false
left = false
right = false

speed = 100

function keyInput(object, event)
	if (event.getKey() == "down") then
		if (event.getAction() == "pressed") then
			down = true;
			object.setFacing("down")
		else
			down = false;
		end
	end
	if (event.getKey() == "up") then
		if (event.getAction() == "pressed") then
			up = true;
			object.setFacing("up")
		else
			up = false;
		end
	end
	if (event.getKey() == "left") then
		if (event.getAction() == "pressed") then
			left = true;
			object.setFacing("left")
		else
			left = false;
		end
	end
	if (event.getKey() == "right") then
		if (event.getAction() == "pressed") then
			right = true;
			object.setFacing("right")
		else
			right = false;
		end
	end
end

function update(object, delta)
	local move_x = 0
	local move_y = 0
	local debuf = false

	if (down) then
		move_y = move_y + 1
	end
	if (up) then
		move_y = move_y - 1
	end
	if (left) then
		move_x = move_x - 1
	end
	if (right) then
		move_x = move_x + 1
	end
	
	if(move_x ~= 0 and move_y ~= 0) then
		debuf = true
	end
	
	
	if (not debuf) then
		object.move(move_x*speed*delta, move_y*speed*delta)
	else
		object.move(move_x*(speed*0.6)*delta, move_y*(speed*0.6)*delta)
	end
end