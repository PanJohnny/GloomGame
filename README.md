# GloomGame
Work in progress game. Uses JFrame and Graphics, whic is not the best pic. Unoptimised and unfinished. Beware that currently there isn't any gameplay available, beause I am working on level designer and things are messy... Enjoy reading my code and feel free to contribute. I am open to new people contributing. If you need anything just [contact me](https://linktr.ee/PanJohnny)

# The PLF format
## Lines
1. PLF_VERSION
2. LEVEL_NAME
3. LEVEL_AUTHOR
4. OBJECTS (surrounded in [])
5. PREFABS (surrounded in [])

## Example

```
0.1
Level 01
PanJohnny
[com.panjohnny.game.tile.Tile(0,0,10,10);;com.panjohnny.game.tile.Tile(0,0,10,10)]
[overlay();;addp(10,10)]
```

## Objects

Objects are defined by a class name with package and a list of parameters. So for instance the Tile is defined like: `com.panjohnny.game.tile.Tile({x},{y},{width},{height})` (instead of the {param} the value).
Objects are separated by `;;` (`,` are used for the parameters).
Illegal characters like `,`, `(`, `)` are prepended with `\` in strings.

## Prefabs

Similar to objects. Prefabs are basically prefabricated methods. They are defined by a name and a list of parameters. So for instance the add player is defined like: `addp({x},{y})` (instead of the {param} the value).

### List of prefabs
 - `addp`: adds a player to the level with the given coordinates
 - `overlay`: adds an overlay to the level
 - `events`: registers as event listener
 - `bl-{type}`: baked lights
   - `bl-round`: adds a round light with the given x, y, radius and color
   - `bl-rect`: adds a rectangle light with the given x, y, width and height and color
   - `bl-rec`: adds a point light with the given x, y, radius and color


# Awesome utilities

## JTPLF
https://panjohnny.github.io/gloom/jtplf
Converts Java to PLF.

Example:
```java
new com.panjohnny.game.tile.Tile(0,0,10,10)
```
```
com.panjohnny.game.tile.Tile(0,0,10,10)
```

Can also convert fields in `Colors` to int rgb