# Jump
Jump is a tile-based platformer engine aimed at early 90's era platformer capabilities.

This project came as the culmination of motivation from working on game jams with some friends, and having tried to make platformers in the past that all have failed. I have tried using modern physics libraries such as box2d for a platformer, but they are over-built for this and always end up having issues when the goal is fine-grained control.

The goals are fairly simple as of yet:

 * Support tile-based levels a la the original Megaman series
 * Have accurate collision resolution
 * Provide parameters to dictate how a body moves in the world
 * Have a level editor to help piece things together

The platformer is in its early stages, and there is plenty that needs to be cleaned up.

The platformer physics are isolated and can be used as a stand alone in any project. The editor is built on top of LibGDX.

The projects are laid out as follows:
* jump-core - Pure platformer engine, no ties to libgdx.
* jump-gdx - libGDX specific things such as image and sound utilities, rendering, etc.
* jump-leveleditor - This project is basically the level editor (jump backend with gdx frontend)
* jump-common - This is a project just for intermediate things, right now it only used for player action state. jump-core sets the 'action state' of some objects (such as player jumping, player running, etc), and these cause events to be fired that can be listened for.


## Working with Intellij
Getting this project up and running in intellij is like a dream.  All you have to do is:
- Tell intellij that you want a new project from Version Control and select the GitHub option
- From there, you might see a popup that says something along the lines of "Convert to Maven Project", go ahead and click the link
- Now, automagically, all of your projects are imported, all the source files are marked, all the asset folders are marked, and you can start programming!

If for some reason, you didn't get the popup about converting to a maven project, and your projects on the left didn't seem to get imported correctly just do this:
- Right click on the root Jump project folder
- Select "Add Framework Support..." from the menu (at the top usually)
- Select the Maven checkbox
- Select next
- And everything should resolve itself.  Intellij sees the poms and uses them to build your *.iml files (or updates them if they are already there)

You should always run a ```mvn clean compile``` before you really get started, just to make sure everything is in order.