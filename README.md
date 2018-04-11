# nonogram

Creates nonogram number coloring puzzles from images or at random.

Example of a nonogram created.
| :row | :0 | :1 | :2 | :3 | :4 | :5 | :6 | :7 | :8 | :9 | :10 | :11 | :12 | :13 | :14 | :15 |
|------+----+----+----+----+----+----+----+----+----+----+-----+-----+-----+-----+-----+-----|
|    0 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|   11 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|   11 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|   11 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|   11 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|   11 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|   32 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|  212 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|    8 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|  421 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|    4 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|    3 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|   41 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|    1 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|    0 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|    0 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|    4 |    |    |    |    |    |    |    |    |    |    |     |     |     |     |     |     |
|      |  1 |  1 |  1 |  1 |  6 |  2 |  5 |  5 |  3 |  1 |   2 |   3 |   1 |   1 |   0 |   0 |
|      |    |  1 |  1 |  2 |    |  1 |    |    |  2 |  3 |   1 |   1 |   1 |     |     |     |
|      |    |  1 |    |    |    |  2 |    |    |    |  1 |     |     |   2 |     |     |     |
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
