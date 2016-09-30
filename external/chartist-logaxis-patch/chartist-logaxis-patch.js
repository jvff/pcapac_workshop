(function (window, document, Chartist) {
	'use strict';


	function AutoScaleAxis(axisUnit, data, chartRect, options) {
		// Usually we calculate highLow based on the data but this can be overriden by a highLow object in the options
		var highLow = options.highLow || Chartist.getHighLow(data.normalized, options, axisUnit.pos);
		this.bounds = Chartist.getBounds(chartRect[axisUnit.rectEnd] - chartRect[axisUnit.rectStart], highLow, options.scaleMinSpace || 20, options.onlyInteger);

		var scale = options.scale || 'linear';
		var match = scale.match(/^([a-z]+)(\d+)?$/);
		this.scale = {
			type: match[1],
			base: Number(match[2]) || 10
		}

		if (this.scale.type === 'log') {
			if (highLow.low * highLow.high <= 0) {
				if (data.normalized.length > 0)
					throw new Error('Negative or zero values are not supported on logarithmic axes.');
				highLow.low = 1;
				highLow.high = 1000;
			}
			var base = this.scale.base;
			var minDecade = Math.floor(baseLog(highLow.low, base));
			var maxDecade = Math.ceil(baseLog(highLow.high, base));
			this.bounds.min = Math.pow(base, minDecade);
			this.bounds.max = Math.pow(base, maxDecade);
			this.bounds.values = [];
			for (var decade = minDecade; decade <= maxDecade; ++decade) {
				this.bounds.values.push(Math.pow(base, decade));
			}
		}

		Chartist.AutoScaleAxis.super.constructor.call(this,
			axisUnit,
			chartRect,
			this.bounds.values,
			options);
	}

	function baseLog(val, base) {
		return Math.log(val) / Math.log(base);
	}

	function projectValue(value) {
		value = +Chartist.getMultiValue(value, this.units.pos);
		var max = this.bounds.max;
		var min = this.bounds.min;
		if (this.scale.type === 'log') {
			var base = this.scale.base;
			return this.axisLength / baseLog(max / min, base) * baseLog(value / min, base);
		}
		return this.axisLength * (value - min) / this.bounds.range;
	}

	Chartist.AutoScaleAxis = Chartist.Axis.extend({
		constructor: AutoScaleAxis,
		projectValue: projectValue
	});

} (window, document, Chartist));