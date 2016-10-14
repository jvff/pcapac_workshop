Loading Kernels
===============

- Starting from a host program and an OpenCL C source file
- Load from file to string
- Create OpenCL program from string
- Build OpenCL program
- Create kernels from compiled OpenCL program

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 350px" data-step="@step"
                src="@routes.Presentations.figure("intro_to_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("program_with_source_file.svg", "0")
        @figure("program_with_source_string.svg", "1")
        @figure("program_with_compute_program.svg", "2")
        @figure("program_with_compiled_program.svg", "3")
        @figure("program_with_kernels.svg", "4")
    </div>
</div>
