Device Memory
=============

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("intro_to_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("device_global_memory.svg", "0")
        @figure("device_memory_cores.svg", "1")
        @figure("device_memory_core.svg", "2")
        @figure("device_local_memory.svg", "3")
        @figure("device_memory_exec_unit.svg", "4")
        @figure("device_private_memory.svg", "5")
        @figure("device_constant_memory.svg", "6")
    </div>
</div>
