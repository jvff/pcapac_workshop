2D Image Dimensions
===================

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("effective_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("image_2d.svg", "0")
        @figure("image_2d_width.svg", "1")
        @figure("image_2d_dimensions.svg", "2")
        @figure("smaller_2d_image.svg", "3")
        @figure("smaller_2d_image_width.svg", "4")
        @figure("smaller_2d_image_dimensions.svg", "5")
        @figure("smaller_2d_image_pitch.svg", "6")
    </div>
</div>
