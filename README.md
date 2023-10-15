## Office Manager 2D version 1.0
Hello, there!

This is initial version of my project OM2D. Currently supported features you can view down

### For all:
#### Features:

1) Build 4 types of rooms in office.
2) View your office information
3) Monitor prices for your rooms
4) Place few items (currently 2, but one with texture)
5) Turn on and turn off the grid
6) Time passes while playing: days, months and years 
7) There are textures for environment objects and for 2 rooms
8) Updated UI

#### Project stack:

1) Java 11 (recommended Adopt)
2) LibGDX
3) SQLite (ORMLite)

But this is not the end! I'm actively developing my project to make it very funny!

Here you can view the screenshot from current version:

![Game Snippet on initial version](assets/game_snippet.png "Game Snippet")

#### Textures, fonts and skins owners:
1) UI skin - Orange by Raymond "Raeleus" Buckley
2) Grass - MiniWorldSprites by Shade and octoshrimpy
3) Font - Upheaval by Brian Kent
4) Road, car, rooms, other - mine

### For developers:

#### Branching and committing
1) When creating new feature/bag, please, create branch from master starting with according keyword ("feature/", "bug/")
2) Commits should be started with task name and after 2 lines must be commit details. For example
```
#123 Add new character

- Add new textures
- Add new actor
```
3) It's recommended to name branch in manner `#123-add-new-character`

#### Pull Requests:
1) PRs must be reviewed and tested. For PR must be at lease 2 PRs
2) PRs can be connected to issues using `Issue name #123`, where #123 issue number in Github issues

#### Work process:
1) No unused imports!
2) In JavaDoc write only description, parameters and return type for methods