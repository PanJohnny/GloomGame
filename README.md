# GloomGame
Work in progress game. Uses JFrame and Graphics, whic is not the best pic. Unoptimised and unfinished. Beware that currently there isn't any gameplay available, beause I am working on level designer and things are messy... Enjoy reading my code and feel free to contribute. I am open to new people contributing. If you need anything just [contact me](https://linktr.ee/PanJohnny)

# The PLF format
## Lines
1. PLF_VERSION
2. LEVEL_NAME
3. LEVEL_AUTHOR
4. OBJECTS (sourounded in [])

## Example

```
0.1
Level 01
PanJohnny
[com.panjohnny.game.tile.Tile(0,0,10,10);;com.panjohnny.game.tile.Tile(0,0,10,10)]
```

## Objects

Objects are defined by a class name with package and a list of parameters. So for instance the Tile is defined like: `com.panjohnny.game.tile.Tile({x},{y},{width},{height})` (instead of the {param} the value).
Objects are separated by `;;` (`,` are used for the parameters).
Illegal characters like `,`, `(`, `)` are prepended with `\` in strings.
