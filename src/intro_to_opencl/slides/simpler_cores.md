Simpler Cores
=============

<div>
    <center>
        <table style="width: 250px; border: 1px solid black">
            <tr>
                <th colspan="2">CPU core</th>
            </tr>
            <tr>
                <td>Superscalar</td>
                <td style="text-align: right">Out-of-order</td>
            </tr>
            <tr>
                <td>Branch prediction</td>
                <td style="text-align: right">Virtualization</td>
            </tr>
            <tr>
                <td>Memory management</td>
                <td style="text-align: right">Threads</td>
            </tr>
        </table>

        <div style="margin: 40px" data-step="1..">
            <img src="@routes.Presentations.figure("intro_to_opencl", "arrow_down.svg")"
                    style="width: 40px">
        </div>

        @simpleCoreCell(step: String) = {
            <td style="border: 1px solid black; text-align: center" data-step="@step">Simple Core</td>
        }

        <table style="width: 250px">
            @for(i <- 1 to 3) {
                <tr>
                    @for(j <- 1 to 3) {
                        @if(i == 2 && j == 2) {
                            @simpleCoreCell("1..")
                        } else {
                            @simpleCoreCell("2..")
                        }
                    }
                </tr>
            }
        </table>
    </center>
</div>
