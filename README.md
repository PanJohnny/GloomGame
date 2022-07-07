# GloomGame
WIP Game

# The PLF format
## Lines
1. PLF
2. PLF_VERSION (current 0.1)
3. LEVEL_NAME
4. LEVEL_AUTHOR
5. OBJECTS (sourounded in [])

## Example

```
PLF
0.1
Level 01
PanJohnny
[com.panjohnny.game.tile.Tile(0,0,10,10);;com.panjohnny.game.tile.Tile(0,0,10,10)]
```

## Objects

Objects are defined by a class name with package and a list of parameters. So for instance the Tile is defined like: `com.panjohnny.game.tile.Tile({x},{y},{width},{height})` (instead of the {param} the value).
Objects are separated by `;;` (`,` are used for the parameters).
Illegal characters like `,`, `(`, `)` are prepended with `\` in strings.
