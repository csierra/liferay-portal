/* global React, AlloyEditor */

(function() {
	'use strict';

	var React = AlloyEditor.React;

	var ButtonAccessibilityImageAlt = React.createClass(
		{
	        mixins: [AlloyEditor.WidgetFocusManager, AlloyEditor.ButtonCfgProps],


			displayName: 'ButtonAccessibilityImageAlt',

			propTypes: {
				editor: React.PropTypes.object.isRequired
			},

			statics: {
				key: 'accessibilityImageAlt'
			},

			/**
	         * Lifecycle. Invoked once, only on the client, immediately after the initial rendering occurs.
	         *
	         * Focuses on the link input to immediately allow editing. This should only happen if the component
	         * is rendered in exclusive mode to prevent aggressive focus stealing.
	         *
	         * @method componentDidMount
	         */
	        componentDidMount: function () {
	            if (this.props.renderExclusive || this.props.manualSelection) {
	                // We need to wait for the next rendering cycle before focusing to avoid undesired
	                // scrolls on the page
	                this._focusLinkInput();
	            }
	        },

			/**
	         * Lifecycle. Invoked once before the component is mounted.
	         * The return value will be used as the initial value of this.state.
	         *
	         * @method getInitialState
	         */
	        getInitialState: function() {
	            // var link = new CKEDITOR.Link(this.props.editor.get('nativeEditor')).getFromSelection();
	            // var href = link ? link.getAttribute('href') : '';
	            // var target = link ? link.getAttribute('target') : this.props.defaultLinkTarget;
	            var image = this.props.editor.get('nativeEditor').getSelection().getSelectedElement();

	            return {
	                element: image,
	                altImage: image.getAttribute('alt')
	            };
	        },

			render: function() {
				return React.createElement(
					'div',
					{
						className: 'ae-container-edit-link'
					},
					React.createElement(
						'div',
						{
							className: 'ae-container-input xxl'
						},
						React.createElement(
							'input',
							{
								className: 'ae-input',
								onChange: this._handleAltChange,
								onKeyDown: this._handleKeyDown,
								placeholder: 'Alt property',
								ref: 'refAltInput',
								value: this.state.altImage
							}
						)
					),
					React.createElement(
						'button',
						{
							className: 'ae-button',
							onClick: this._updateAltImage
						},
						React.createElement(
							'span',
							{
								className: 'ae-icon-ok'
							}
						)
					)
				);
			},

			/**
	         * Focuses the user cursor on the widget's input.
	         *
	         * @protected
	         * @method _focusLinkInput
	         */
	        _focusLinkInput: function() {
	            var instance = this;

	            var focusLinkEl = function() {
	                AlloyEditor.ReactDOM.findDOMNode(instance.refs.refAltInput).focus();
	            };

	            if (window.requestAnimationFrame) {
	                window.requestAnimationFrame(focusLinkEl);
	            } else {
	                setTimeout(focusLinkEl, 0);
	            }

	        },

	        _handleAltChange: function(event) {
	        	this.setState({
	                altImage: event.target.value
	            });

	            this._focusLinkInput();
	        },

			_handleKeyDown: function(event) {
				if (event.keyCode === 13) {
					event.preventDefault();
                	this._updateAltImage();
	            }
			},

			_updateAltImage: function() {
				var editor = this.props.editor.get('nativeEditor');

				var newValue = this.refs.refAltInput.value;

				this.setState(
					{
						altImage: newValue
					}
				);

				this.state.element.setAttribute('alt', newValue);

				editor.fire('actionPerformed', this);

	            // We need to cancelExclusive with the bound parameters in case the button is used
	            // inside another in exclusive mode (such is the case of the link button)
	            this.props.cancelExclusive();

			}
		}
	);

	AlloyEditor.Buttons[ButtonAccessibilityImageAlt.key] = AlloyEditor.ButtonAccessibilityImageAlt = ButtonAccessibilityImageAlt;
}());