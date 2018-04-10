# nonogram

Creates nonogram number coloring puzzles from images or at random.

## Usage

$ lein run first-argument second-argument

* first-argument: path to the image-file
* second-argument: max-width for the image, gets resized if larger than this

## Examples
Also useful with $lein repl

Then you can use the methods directly, like (random-nonogram size) where size
is the integer number for the size of the random image to be created and turned 
into a nonogram.

You can also create nonogram from huge images with (image-nonogram path), but be 
warned, it takes a while and the end result is really confusing to view from a terminal.

You can also view the art once you have created it with (print-art)

For larger nonograms, you can't see the end result clearly so I suggest throwing
the result to a file and then viewing it in an editor that has better viewing 
capabilities.

$ lein run ./image-path 64 > resulting-nonogram.txt 

### Bugs

No clean exception handling for file not found and such.

### TODO
Add better command-line usage for the non repl users

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
