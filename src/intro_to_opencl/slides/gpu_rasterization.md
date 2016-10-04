Graphics Rasterization
======================

- GPUs are devices specific to draw graphics
- Each pixel can be processed independently
- A Full HD screen has 2 million pixels
- The screen is generally updated every 16 milliseconds

<div>
    <center>
        @pixel(color: String) = {
            <td style="height: 1em; border: 1px solid black; text-align: center; background-color: @color"></td>
        }

        @rasterPixel(x: Int, y: Int) = @{
            val x1 = 12;
            val y1 = 2;

            val x2 = 4;
            val y2 = 17;

            val x3 = 18;
            val y3 = 14;

            val u1x = y2 - y1;
            val u1y = -x2 + x1;

            val u2x = y3 - y2;
            val u2y = -x3 + x2;

            val u3x = y1 - y3;
            val u3y = -x1 + x3;

            val v1x = x - x1;
            val v1y = y - y1;

            val v2x = x - x2;
            val v2y = y - y2;

            val v3x = x - x3;
            val v3y = y - y3;

            val dot1 = u1x * v1x + u1y * v1y;
            val dot2 = u2x * v2x + u2y * v2y;
            val dot3 = u3x * v3x + u3y * v3y;

            if (dot1 >= 0 && dot2 >= 0 && dot3 >= 0)
                pixel("green");
            else
                pixel("white");
        }

        <table style="width: 20em; border-collapse: collapse">
            @for(i <- 1 to 20) {
                <tr>
                    @for(j <- 1 to 20) {
                        @rasterPixel(i, j)
                    }
                </tr>
            }
        </table>
    </center>
</div>
