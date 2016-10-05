OpenCL
======

- Framework for programming heterogeneous computers
    - CPUs, GPUs, co-processors, FGPAs, ...
- Defines a language to implement tasks: OpenCL C
- Contains a library to manage task executions

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 400px" data-step="@step"
                src="@routes.Presentations.figure("intro_to_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("opencl_program.svg", "0")
        @figure("opencl_devices.svg", "1")
        @figure("opencl_tasks.svg", "2")
        @figure("opencl_full.svg", "3")
    </div>
</div>
