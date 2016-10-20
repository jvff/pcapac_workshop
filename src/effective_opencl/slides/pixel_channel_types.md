Pixel Channel Types
===================

- There are different types of channels
- Given a channel with `n` bits
    - Unsigned int values: \\(0 \to 2^n - 1\\)
    - Signed int values: \\(-2^{n - 1} \to 2^{n - 1}\\)
- Floating point channel types
    - 16 bit half-precision
    - 32 bit single-precision
- Normalized channel values
    - Store as signed or unsigned integers
    - Use as floating point value
        - \\(0 \to 1.0\\) for unsigned channels
        - \\(-1.0 \to 1.0\\) for signed channels

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("effective_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("single_channel.svg", "0")
        @figure("channel_bits.svg", "1..")
    </div>

    <script type="text/javascript">
        MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
    </script>
</div>
