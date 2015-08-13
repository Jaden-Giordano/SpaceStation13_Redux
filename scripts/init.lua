function init(game)
    print("hi")
    game.linkIdToTile(1, {lua="tile/floor", image="sheets/base.png", sheetId=20})
    game.linkIdToTile(2, "tile/wall")
    game.linkIdToTile(3, "tile/door")
end