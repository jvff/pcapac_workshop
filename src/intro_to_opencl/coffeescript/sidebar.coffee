use_secure_protocol = document.location.protocol is 'https:'

editor = ace.edit('editor-container')

editor.setTheme('ace/theme/monokai')
editor.getSession().setMode('ace/mode/c_cpp')

editor_container = document.getElementById 'editor-container'
terminal_container = document.getElementById 'terminal-container'

program_tab = document.getElementById 'program_tab'
kernel_tab = document.getElementById 'kernel_tab'
terminal_tab = document.getElementById 'terminal_tab'

current_tab = program_tab

change_to_tab = (tab) ->
    current_tab.className = ''
    tab.className = 'selected'

    current_tab = tab

save_code = ->

program_cursor = 0
program_code = """
    #include <stdio.h>

    int main(int argc, char* argv[]) {
        printf("Let's start coding with OpenCL!");

        return 0;
    }
    """

kernel_cursor = 0
kernel_code = """
    // Compute kernel
    """

save_program_code = ->
    program_code = editor.getValue()
    program_cursor = editor.getCursorPosition()
    save_file('program.c', program_code)

save_kernel_code = ->
    kernel_code = editor.getValue()
    kernel_cursor = editor.getCursorPosition()
    save_file('kernel.cl', kernel_code)

save_file = (path, contents) ->
    req = new XMLHttpRequest()
    terminal_id = window.terminal.get_terminal_id()
    url = jsRoutes.controllers.Terminal.upload(terminal_id, path)

    req.open 'POST', url.absoluteURL(use_secure_protocol)
    req.send contents

set_code = (code, cursor) ->
    editor.setValue(code)
    editor.clearSelection()
    editor.moveCursorToPosition(cursor)

change_to_program_tab = ->
    change_to_tab(program_tab)
    hide_terminal()
    show_editor(program_code, program_cursor, save_program_code)

change_to_kernel_tab = ->
    change_to_tab(kernel_tab)
    hide_terminal()
    show_editor(kernel_code, kernel_cursor, save_kernel_code)

change_to_terminal_tab = ->
    change_to_tab(terminal_tab)
    hide_editor()
    show_terminal()

show_terminal = ->
    terminal_container.style.display = 'block'
    window.terminal.resize_terminal()

hide_terminal = ->
    terminal_container.style.display = 'none'

show_editor = (new_code, cursor_position, new_code_saver) ->
    save_code()
    save_code = new_code_saver
    set_code(new_code, cursor_position)

    editor_container.style.display = 'block'

hide_editor = ->
    save_code()
    editor_container.style.display = 'none'

program_tab.addEventListener('click', change_to_program_tab)
kernel_tab.addEventListener('click', change_to_kernel_tab)
terminal_tab.addEventListener('click', change_to_terminal_tab)

change_to_terminal_tab()
