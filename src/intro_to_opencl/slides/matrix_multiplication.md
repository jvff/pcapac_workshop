Matrix Multiplication
=====================

<div>
    <p>$$ \mathbf{A}_{40 \times 30} \times \mathbf{B}_{30 \times 20} = \mathbf{C}_{40 \times 20} $$</p>

    <table data-step="1" style="border-spacing: 0">
        <tr style="height: 0.25em">
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>

            <td rowspan="7">\( \times \)</td>

            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>

            <td rowspan="7">\( = \)</td>

            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>
        </tr>
        <tr>
            <td>\( a_{1,1 } \)</td>
            <td>\( a_{1,2 } \)</td>
            <td>\( a_{1,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{1,30 } \)</td>

            <td>\( b_{1,1 } \)</td>
            <td>\( b_{1,2 } \)</td>
            <td>\( b_{1,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{1,20 } \)</td>

            <td>\( c_{1,1 } \)</td>
            <td>\( c_{1,2 } \)</td>
            <td>\( c_{1,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{1,40 } \)</td>
        </tr>
        <tr>
            <td>\( a_{2,1 } \)</td>
            <td>\( a_{2,2 } \)</td>
            <td>\( a_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{2,30 } \)</td>

            <td>\( b_{2,1 } \)</td>
            <td>\( b_{2,2 } \)</td>
            <td>\( b_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{2,20 } \)</td>

            <td>\( c_{2,1 } \)</td>
            <td>\( c_{2,2 } \)</td>
            <td>\( c_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{2,20 } \)</td>
        </tr>
        <tr>
            <td>\( a_{3,1 } \)</td>
            <td>\( a_{3,2 } \)</td>
            <td>\( a_{3,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{3,30 } \)</td>

            <td>\( b_{3,1 } \)</td>
            <td>\( b_{3,2 } \)</td>
            <td>\( b_{3,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{3,20 } \)</td>

            <td>\( c_{3,1 } \)</td>
            <td>\( c_{3,2 } \)</td>
            <td>\( c_{3,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{3,20 } \)</td>
        </tr>
        <tr>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>

            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>

            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>
        </tr>
        <tr>
            <td>\( a_{40,1 } \)</td>
            <td>\( a_{40,2 } \)</td>
            <td>\( a_{40,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{40,30 } \)</td>

            <td>\( b_{30,1 } \)</td>
            <td>\( b_{30,2 } \)</td>
            <td>\( b_{30,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{30,20 } \)</td>

            <td>\( c_{40,1 } \)</td>
            <td>\( c_{40,2 } \)</td>
            <td>\( c_{40,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{40,20 } \)</td>
        </tr>
        <tr style="height: 0.25em">
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>

            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>

            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </table>

    <table data-step="2" style="border-spacing: 0">
        <tr style="height: 0.25em">
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>

            <td rowspan="7">\( \times \)</td>

            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>

            <td rowspan="7">\( = \)</td>

            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>
        </tr>
        <tr>
            <td style="border-left: 2px solid blue; border-top: 2px solid blue; border-bottom: 2px solid blue">\( a_{1,1 } \)</td>
            <td style="border-top: 2px solid blue; border-bottom: 2px solid blue">\( a_{1,2 } \)</td>
            <td style="border-top: 2px solid blue; border-bottom: 2px solid blue">\( a_{1,3 } \)</td>
            <td style="border-top: 2px solid blue; border-bottom: 2px solid blue">\( \cdots \)</td>
            <td style="border-right: 2px solid blue; border-top: 2px solid blue; border-bottom: 2px solid blue">\( a_{1,30 } \)</td>

            <td style="border-top: 2px solid blue; border-left: 2px solid blue; border-right: 2px solid blue">\( b_{1,1 } \)</td>
            <td>\( b_{1,2 } \)</td>
            <td>\( b_{1,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{1,20 } \)</td>

            <td style="border: 2px solid blue">\( c_{1,1 } \)</td>
            <td>\( c_{1,2 } \)</td>
            <td>\( c_{1,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{1,40 } \)</td>
        </tr>
        <tr>
            <td>\( a_{2,1 } \)</td>
            <td>\( a_{2,2 } \)</td>
            <td>\( a_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{2,30 } \)</td>

            <td style="border-left: 2px solid blue; border-right: 2px solid blue">\( b_{2,1 } \)</td>
            <td>\( b_{2,2 } \)</td>
            <td>\( b_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{2,20 } \)</td>

            <td>\( c_{2,1 } \)</td>
            <td>\( c_{2,2 } \)</td>
            <td>\( c_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{2,20 } \)</td>
        </tr>
        <tr>
            <td>\( a_{3,1 } \)</td>
            <td>\( a_{3,2 } \)</td>
            <td>\( a_{3,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{3,30 } \)</td>

            <td style="border-left: 2px solid blue; border-right: 2px solid blue">\( b_{3,1 } \)</td>
            <td>\( b_{3,2 } \)</td>
            <td>\( b_{3,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{3,20 } \)</td>

            <td>\( c_{3,1 } \)</td>
            <td>\( c_{3,2 } \)</td>
            <td>\( c_{3,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{3,20 } \)</td>
        </tr>
        <tr>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>

            <td style="border-left: 2px solid blue; border-right: 2px solid blue">\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>

            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>
        </tr>
        <tr>
            <td>\( a_{40,1 } \)</td>
            <td>\( a_{40,2 } \)</td>
            <td>\( a_{40,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{40,30 } \)</td>

            <td style="border-bottom: 2px solid blue; border-left: 2px solid blue; border-right: 2px solid blue">\( b_{30,1 } \)</td>
            <td>\( b_{30,2 } \)</td>
            <td>\( b_{30,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{30,20 } \)</td>

            <td>\( c_{40,1 } \)</td>
            <td>\( c_{40,2 } \)</td>
            <td>\( c_{40,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{40,20 } \)</td>
        </tr>
        <tr style="height: 0.25em">
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>

            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>

            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </table>

    <table data-step="3" style="border-spacing: 0">
        <tr style="height: 0.25em">
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>

            <td rowspan="7">\( \times \)</td>

            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>

            <td rowspan="7">\( = \)</td>

            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-left: 1px solid black; border-bottom: 1px solid black"></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td rowspan="7" style="width: 6px; border-top: 1px solid black; border-right: 1px solid black; border-bottom: 1px solid black"></td>
        </tr>
        <tr>
            <td style="border-left: 2px solid blue; border-top: 2px solid blue; border-bottom: 2px solid blue">\( a_{1,1 } \)</td>
            <td style="border-top: 2px solid blue; border-bottom: 2px solid blue">\( a_{1,2 } \)</td>
            <td style="border-top: 2px solid blue; border-bottom: 2px solid blue">\( a_{1,3 } \)</td>
            <td style="border-top: 2px solid blue; border-bottom: 2px solid blue">\( \cdots \)</td>
            <td style="border-right: 2px solid blue; border-top: 2px solid blue; border-bottom: 2px solid blue">\( a_{1,30 } \)</td>

            <td style="border-top: 2px solid blue; border-left: 2px solid blue; border-right: 2px solid blue">\( b_{1,1 } \)</td>
            <td>\( b_{1,2 } \)</td>
            <td style="border-top: 2px solid red; border-left: 2px solid red; border-right: 2px solid red">\( b_{1,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{1,20 } \)</td>

            <td style="border: 2px solid blue">\( c_{1,1 } \)</td>
            <td>\( c_{1,2 } \)</td>
            <td>\( c_{1,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{1,40 } \)</td>
        </tr>
        <tr>
            <td>\( a_{2,1 } \)</td>
            <td>\( a_{2,2 } \)</td>
            <td>\( a_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{2,30 } \)</td>

            <td style="border-left: 2px solid blue; border-right: 2px solid blue">\( b_{2,1 } \)</td>
            <td>\( b_{2,2 } \)</td>
            <td style="border-left: 2px solid red; border-right: 2px solid red">\( b_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{2,20 } \)</td>

            <td>\( c_{2,1 } \)</td>
            <td>\( c_{2,2 } \)</td>
            <td>\( c_{2,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{2,20 } \)</td>
        </tr>
        <tr>
            <td style="border-left: 2px solid red; border-top: 2px solid red; border-bottom: 2px solid red">\( a_{3,1 } \)</td>
            <td style="border-top: 2px solid red; border-bottom: 2px solid red">\( a_{3,2 } \)</td>
            <td style="border-top: 2px solid red; border-bottom: 2px solid red">\( a_{3,3 } \)</td>
            <td style="border-top: 2px solid red; border-bottom: 2px solid red">\( \cdots \)</td>
            <td style="border-right: 2px solid red; border-top: 2px solid red; border-bottom: 2px solid red">\( a_{3,30 } \)</td>

            <td style="border-left: 2px solid blue; border-right: 2px solid blue">\( b_{3,1 } \)</td>
            <td>\( b_{3,2 } \)</td>
            <td style="border-left: 2px solid red; border-right: 2px solid red">\( b_{3,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{3,20 } \)</td>

            <td>\( c_{3,1 } \)</td>
            <td>\( c_{3,2 } \)</td>
            <td style="border: 2px solid red">\( c_{3,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{3,20 } \)</td>
        </tr>
        <tr>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>

            <td style="border-left: 2px solid blue; border-right: 2px solid blue">\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td style="border-left: 2px solid red; border-right: 2px solid red">\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>

            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \vdots \)</td>
            <td>\( \ddots \)</td>
            <td>\( \vdots \)</td>
        </tr>
        <tr>
            <td>\( a_{40,1 } \)</td>
            <td>\( a_{40,2 } \)</td>
            <td>\( a_{40,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( a_{40,30 } \)</td>

            <td style="border-bottom: 2px solid blue; border-left: 2px solid blue; border-right: 2px solid blue">\( b_{30,1 } \)</td>
            <td>\( b_{30,2 } \)</td>
            <td style="border-bottom: 2px solid red; border-left: 2px solid red; border-right: 2px solid red">\( b_{30,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( b_{30,20 } \)</td>

            <td>\( c_{40,1 } \)</td>
            <td>\( c_{40,2 } \)</td>
            <td>\( c_{40,3 } \)</td>
            <td>\( \cdots \)</td>
            <td>\( c_{40,20 } \)</td>
        </tr>
        <tr style="height: 0.25em">
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>

            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>

            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
    </table>

    <script type="text/javascript">
        MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
    </script>
</div>
