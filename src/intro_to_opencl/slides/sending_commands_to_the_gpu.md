Sending Commands to the GPU
===========================

- How does the host program communicate with the GPU?
- Through command-queues
- Commands are pushed by the host
- And popped by the device

<div>
    @figure(figure: String, step: String) = {
        <img style="width: 400px" data-step="@step"
                src="@routes.Presentations.figure("intro_to_opencl", figure)">
    }

    <div style="margin-left: 100px">
        @figure("no_command_queue.svg", "0")
        @figure("command_queue.svg", "1")
        @figure("command_queue_with_tasks.svg", "2")
        @figure("command_queue_after_first_dispatch.svg", "3")
    </div>
</div>
