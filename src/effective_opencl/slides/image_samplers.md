Image Samplers
==============

- Configuration on how to obtain pixels
- Three parameters
    - Coordinate type
        - Integer
        - Float
    - Out of bounds access
        - Clamp at image limits
        - Clamp at border
        - Repeat
        - Mirrored repeat
    - Filtering mode
        - Nearest pixel
        - Linear interpolation

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("effective_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("image_sample.svg", "0..2")
        @figure("integer_sample_coord.svg", "3")
        @figure("float_sample_coord.svg", "4")
        @figure("image_out_of_bounds.svg", "5")
        @figure("image_clamp_edge.svg", "6")
        @figure("image_clamp_border.svg", "7")
        @figure("image_repeat.svg", "8")
        @figure("image_repeat_mirror.svg", "9")
        @figure("image_filter.svg", "10")
        @figure("image_filter_nearest.svg", "11")
        @figure("image_filter_linear.svg", "12")
    </div>

    <script type="text/javascript">
        MathJax.Hub.Queue(["Typeset", MathJax.Hub]);
    </script>
</div>
