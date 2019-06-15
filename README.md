# nonogram

Creates nonogram number coloring puzzles from images or at random.

Example of a nonogram created.

## Usage

```bash
lein run first-argument second-argument third-argument last-argument
```

* first-argument: path to the image-file (lein run root folder is ./src) or random
* second-argument: string "draw" if you want to draw the ascii art of the nonogram
* third-argument: map for the ascii art with keys :0 and :1. :1 value is used for the grey colored parts and :0 is used for the white coloder parts {:0 "O" :1 :"X"}. Used in a "draw" run.
* last-argument: max-width for the image, gets resized if larger than this, always required

```bash
lein run "face.png" 16
lein run "face.png" "draw" 16 
lein run "face.png" "{:0 Z :1 @}" 16 
lein run "random" 16
lein run "random" "draw" 16
lein run "random" "draw" "{:0 Z :1 @}" 16
```

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
Odd inputs can result in odd exceptions.

### TODO
* Optimize running times
* Refactor main function

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
