Host and Device Memory
======================

- Shared memory (integrated GPU)
- Separate memory (discrete GPU)

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("intro_to_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("host_device_shared_memory.svg", "0")
        @figure("host_device_separate_memory.svg", "1")
        @figure("host_memory_with_object.svg", "2")
        @figure("host_device_memory_with_object.svg", "3")
        @figure("host_to_device_memory_transfer.svg", "4")
    </div>
</div>
