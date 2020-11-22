package com.owncloud.android.ui.dialog

import android.app.Dialog
import android.os.Bundle
import com.owncloud.android.R

class ChangeAccountTipDialog : ConfirmationDialogFragment(),
    ConfirmationDialogFragment.ConfirmationDialogFragmentListener {

    private var listener: OnConfirmationListener? = null

    companion object {

        fun newInstance(): ChangeAccountTipDialog {
            val fra = ChangeAccountTipDialog()
            val args = Bundle()
            args.putInt(ARG_POSITIVE_BTN_RES, R.string.common_ok)
            args.putInt(ARG_NEGATIVE_BTN_RES, R.string.common_cancel)
            args.putInt(ARG_MESSAGE_RESOURCE_ID, R.string.change_account_tip)
            fra.arguments = args
            return fra
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOnConfirmationListener(this)

    }

    fun setOnConfirmationListener(listener: OnConfirmationListener?) {
        this.listener = listener
    }

    override fun onConfirmation(callerTag: String?) {
        listener?.onConfirmation()
    }

    override fun onNeutral(callerTag: String?) {
        dismiss()
    }

    override fun onCancel(callerTag: String?) {
        dismiss()
    }

    interface OnConfirmationListener {
        fun onConfirmation()
    }
}
