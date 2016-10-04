The Future is Parallel
======================

- Hybrid approach
- Different tools for different tasks
- Heterogeneous computing

<div>
    @simpleCoreCell() = {
        <td style="border: 1px solid black; text-align: center">Simple Core</td>
    }

    <center style="margin-top: 2em">
        <table style="border: 1px solid black">
            <tr>
                <td>
                    @for(core <- 1 to 2) {
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
                    }
                </td>
                <td>
                    <table style="width: 250px">
                        @for(i <- 1 to 6) {
                            <tr>
                                @for(j <- 1 to 3) {
                                    @simpleCoreCell()
                                }
                            </tr>
                        }
                    </table>
                </td>
            </tr>
        </table>
    </center>
</div>
