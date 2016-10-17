Image Pixel Format
==================

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("effective_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("image_format_rgba.svg", "0")
        @figure("image_format_argb.svg", "1")
        @figure("image_format_rgb.svg", "2")
        @figure("image_format_rgb_32.svg", "3")
        @figure("image_format_rg.svg", "4")
        @figure("image_format_r.svg", "5")
    </div>
</div>
