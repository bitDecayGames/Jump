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
