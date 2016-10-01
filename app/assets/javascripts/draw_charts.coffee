draw_charts = ->
    charts = document.getElementsByClassName("chart")

    for chart in charts
        load_and_draw_chart(chart)

load_and_draw_chart = (container) ->
    if container.hasAttribute("data-src")
        chart_url = container.getAttribute("data-src")
        download_data(chart_url, (chart) -> draw_chart(container, chart))

download_data = (url, action) ->
    req = new XMLHttpRequest()

    req.addEventListener 'readystatechange', ->
        if req.readyState is 4
            successResultCodes = [200, 304]

            if req.status in successResultCodes
                action(req.responseText)

    req.open 'GET', url, true
    req.send()

draw_chart = (container, chart_json) ->
    chart = JSON.parse(chart_json)
    data = parse_chart_data(chart.data)
    options = parse_chart_options(chart.options, chart.data)

    if chart.type is 'line'
        new Chartist.Line("##{container.id}", data, options)

parse_chart_data = (chart_data) ->
    data =
        labels: []
        series: [[]]

    for data_point in chart_data
        for label,value of data_point
            x_value = parseInt(label, 10)

            if typeof value is 'string'
                y_value = value
            else
                y_value = value[0]

            point =
                x: x_value
                y: y_value

            data.labels.push x_value
            data.series[0].push point

    return data

parse_chart_options = (chart_options, chart_data) ->
    options = {}

    for option,value of chart_options
        if option is 'plugins'
            options.plugins = parse_chart_plugins(value, chart_data)
        else if option is 'axisX'
            options.axisX = parse_axis(value)
        else if option is 'axisY'
            options.axisY = parse_axis(value)
        else
            options[option] = value

    return options

parse_chart_plugins = (chart_plugins, chart_data) ->
    plugins = []

    for plugin,value of chart_plugins
        if plugin is 'ctPointLabels'
            options = parse_point_label_plugin_options(value, chart_data)
            plugins.push Chartist.plugins.ctPointLabels(options)

    return plugins

parse_point_label_plugin_options = (options, chart_data) ->
    point_labels = new Map()

    for data_point in chart_data
        for x_value,y_value_and_point_label of data_point
            y_value = y_value_and_point_label[0]
            point_label = y_value_and_point_label[1]

            point_labels.set("#{x_value}, #{y_value}", point_label)

    options.labelInterpolationFnc = (value) -> point_labels.get(value)

    return options

parse_axis = (requested_options) ->
    options = {}

    for option,value of requested_options
        if option is 'type'
            options.type = parse_axis_type(value)
        else if option is 'format'
            options.labelInterpolationFnc = parse_axis_format(value)
        else
            options[option] = value

    return options

parse_axis_type = (requested_type) ->
    if requested_type is 'Chartist.AutoScaleAxis'
        return Chartist.AutoScaleAxis
    if requested_type is 'Chartist.FixedScaleAxis'
        return Chartist.FixedScaleAxis
    else
        return null

parse_axis_format = (requested_format) ->
    if requested_format is 'with_order_suffixes'
        return order_suffixes_interpolation
    else if requested_format is 'meter_scale'
        return meter_scale_interpolation
    else
        return default_interpolation

order_suffixes_interpolation = (value) ->
    if value < 1000
        return value
    else if value < 1000000
        return (value/1000) + 'K'
    else if value < 1000000000
        return (value/1000000) + 'M'
    else
        return (value/1000000000) + 'B'

meter_scale_interpolation = (value) ->
    if value < 1e-6
        return (value*1000000000) + 'nm'
    else if value < 1e-3
        return (value*1000000) + 'µm'
    else if value < 1
        return (value*1000) + 'mm'
    else if value < 1000
        return value + 'm'
    else if value < 1000000
        return (value/1000) + 'Km'
    else if value < 1000000000
        return (value/1000000) + 'Mm'
    else
        return (value/1000000000) + 'Gm'

draw_charts()
