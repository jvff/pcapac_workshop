Conway's Game of Life
=====================

- In an infinite 2D grid
- Cells are either active or inactive
- Every iteration
    - Active cells with less than 2 active neighbors deactivate
    - Active cells with 2 or 3 active neighbors continue active
    - Active cells with more than 3 active neighbors deactivate
    - Inactive cells with 3 active neighbors activate

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("effective_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("conway_grid.svg", "0")
        @figure("conway_toad.svg", "1..2")
        @figure("conway_toad_underpopulation.svg", "3")
        @figure("conway_toad_survivers.svg", "4")
        @figure("conway_toad_overpopulation.svg", "5")
        @figure("conway_toad_birth.svg", "6")
        @figure("conway_toad_2.svg", "7,9")
        @figure("conway_toad.svg", "8,10")
    </div>
</div>
