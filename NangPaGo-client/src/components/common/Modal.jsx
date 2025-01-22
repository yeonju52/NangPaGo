import React from 'react';
import PropTypes from 'prop-types';
import ReactDOM from 'react-dom';

function Modal({ isOpen, onClose, title, description, children, buttons }) {
  if (!isOpen) return null;

  const {
    primary: { text: primaryButtonText = '확인', onClick: primaryButtonAction = onClose } = {},
    secondary: { text: secondaryButtonText, onClick: secondaryButtonAction } = {},
  } = buttons || {};

  return ReactDOM.createPortal(
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
      <div className="bg-white p-8 rounded-lg relative flex flex-col items-center max-w-[300px] w-[calc(100%-32px)]">
        {title && (
          <p className="text-center text-lg font-semibold text-text-600 mb-2">{title}</p>
        )}
        {description && (
          <p className="text-center text-sm text-gray-500">{description}</p>
        )}
        <div>{children}</div>
        <div className="flex justify-between mt-4">
          {secondaryButtonText && secondaryButtonAction && (
            <button
              onClick={secondaryButtonAction}
              className="bg-gray-300 text-black px-5 py-3 rounded-lg mr-2"
            >
              {secondaryButtonText}
            </button>
          )}
          <button
            onClick={primaryButtonAction}
            className={`bg-primary text-white px-5 py-3 rounded-lg ${secondaryButtonText ? 'ml-2' : ''}`}
          >
            {primaryButtonText}
          </button>
        </div>
      </div>
    </div>,
    document.body
  );
}

Modal.propTypes = {
  isOpen: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  children: PropTypes.node.isRequired,
  title: PropTypes.string,
  buttons: PropTypes.shape({
    primary: PropTypes.shape({
      text: PropTypes.string,
      onClick: PropTypes.func,
    }),
    secondary: PropTypes.shape({
      text: PropTypes.string,
      onClick: PropTypes.func,
    }),
  }),
};

export default Modal;
