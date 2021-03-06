Simple Matrix Multiplication Kernel
===================================

- Run kernel for every output matrix element
- Inputs:
    - Matrix \\( \mathbf{A} \\)
    - Matrix \\( \mathbf{B} \\)
    - Output Matrix \\( \mathbf{C} \\)
    - Output element row \\(i\\) and column \\(j\\)
    - Number of columns \\(B_{cols}\\) in matrix \\( \mathbf{B} \\)
    - Number of elements to multiply \\( N \\)
- Algorithm:
    - \\( c_{i,j} \leftarrow 0 \\)
    - for \\( k \\) from \\( 1 \\) to \\( N \\)
        - \\( c_{i,j} \leftarrow c_{i,j} + a_{i,k} * b_{k,j} \\)

<div>
    <script type="text/javascript">
        MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
    </script>
</div>
