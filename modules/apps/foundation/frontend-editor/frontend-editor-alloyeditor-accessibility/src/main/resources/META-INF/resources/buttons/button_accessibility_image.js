/* global React, AlloyEditor */

(function() {
	'use strict';

	var React = AlloyEditor.React;

	var ButtonAccessibilityImage = React.createClass(
		{
			mixins: [AlloyEditor.ButtonStateClasses, AlloyEditor.ButtonCfgProps],

			displayName: 'ButtonAccessibilityImage',

			propTypes: {
				editor: React.PropTypes.object.isRequired
			},

			statics: {
				key: 'accessibilityImage'
			},

			/**
	         * Lifecycle. Renders the UI of the button.
	         *
	         * @method render
	         * @return {Object} The content which should be rendered.
	         */
	        render: function() {
	            var cssClass = 'ae-button ' + this.getStateClasses();

	            if (this.props.renderExclusive) {
	                var props = this.mergeButtonCfgProps();

	                return React.createElement(
	                    AlloyEditor.ButtonAccessibilityImageAlt,
	                    props
	                );
	            } else {
	                return React.createElement(
	                	'button',
	                	{
	                		className: cssClass,
	                		onClick: this._requestExclusive
	                	},
	                	React.createElement(
	                		'span',
	                		{
	                			className: 'ae-icon-link'
	                		}
	                	)
	                );
	            }
	        },

	        /**
	         * Requests the link button to be rendered in exclusive mode to allow the creation of a link.
	         *
	         * @protected
	         * @method _requestExclusive
	         */
	        _requestExclusive: function() {
	            this.props.requestExclusive(ButtonAccessibilityImage.key);
	        }
		}
	);

	AlloyEditor.Buttons[ButtonAccessibilityImage.key] = AlloyEditor.ButtonAccessibilityImage = ButtonAccessibilityImage;
}());