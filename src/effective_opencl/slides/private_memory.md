Private Memory
==============

- Normally mapped to execution unit registers
- All function scope variables are usually stored in registers

<div>
    <pre class="prettyprint lang-c linenums" data-step="0">__kernel void computeKernel() {
    // ...
}
    </pre>

    <pre class="prettyprint lang-c linenums" data-step="1">__kernel void computeKernel() {
    int variable;

    // ...
}
    </pre>

    <pre class="prettyprint lang-c linenums" data-step="2">__kernel void computeKernel() {
    int variable;
    float array[10];

    // ...
}
    </pre>

    <pre class="prettyprint lang-c linenums" data-step="3">__kernel void computeKernel(double value) {
    int variable;
    float array[10];

    // ...
}
    </pre>

    <script type="text/javascript">
        PR.prettyPrint();
    </script>
</div>
