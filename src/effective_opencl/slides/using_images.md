Using Images
============

<div>
    <ul>
        <li>GPU: G is for graphics</li>
        <li>They can optimize how they are stored</li>
        <li data-step="5..">They can compress them</li>
        <li>They can have special units to handle images</li>
        <li>They can interpolate between pixels</li>
    </ul>

    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("effective_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("image_store_scanline.svg", "1")
        @figure("image_store_scancolumn.svg", "2")
        @figure("image_store_diagonal.svg", "3")
        @figure("image_store_space_filling.svg", "4")
        @figure("image_store_compressed.svg", "5")
        @figure("device_texture_unit.svg", "6")
        @figure("pixel_interpolation.svg", "7")
    </div>
</div>
