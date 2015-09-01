# Jump
Jump is a tile-based platformer engine aimed at early 90's era platformer capabilities.

This project came as the culmination of motivation from working on game jams with some friends, and having tried to make platformers in the past that all have failed. I have tried using modern physics libraries such as box2d for a platformer, but they are over-built for this and always end up having issues when the goal is fine-grained control.

The goals are fairly simple as of yet:

 * Support tile-based levels a la the original Megaman series
 * Have accurate collision resolution.
 * Provide parameters to dictate how a body moves in the world
 * Have a level editor to help piece things together

The platformer is in its early stages, and there is plenty that needs to be cleaned up.

The platformer physics are isolated and can be used as a stand alone in any project. The editor is built on top of LibGDX.

The projects are laid out as follows:
* bitDecayJump - Pure platformer engine, no ties to libgdx.
* bitDecayGDX - libGDX specific things such as image and sound utilities, rendering, etc.
* bitDecayJumpLibGDX - This project is basically the level editor (jump backend with gdx frontend)
* bitDecayCommon - This is a project just for intermediate things, right now it only has state objects in it (state would be set by Jump, but used by some external code) I wanted to have the code visible to any project that needs it without having dependencies against one of the other projects.
