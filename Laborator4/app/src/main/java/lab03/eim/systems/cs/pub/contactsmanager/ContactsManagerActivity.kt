package lab03.eim.systems.cs.pub.contactsmanager

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.SyncStateContract.Constants
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import lab03.eim.systems.cs.pub.contactsmanager.databinding.ActivityContactsManagerBinding
import java.util.*


class ContactsManagerActivity : AppCompatActivity() {

    lateinit var binding: ActivityContactsManagerBinding
    val CONTACTS_MANAGER_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        if (intent != null) {
            val phone =
                intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY")
            if (phone != null) {
                binding.textPhone.setText(phone)
            } else {
                Toast.makeText(this, resources.getString(R.string.phone_error), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun changeVisibilityOfAdditionalFields(view: View) {
        if (!binding.layoutAdditionalFields.isVisible) {
            binding.layoutAdditionalFields.visibility = View.VISIBLE
            (view as Button).text = resources.getText(R.string.hide_additional)
        } else {
            binding.layoutAdditionalFields.visibility = View.INVISIBLE
            (view as Button).text = resources.getText(R.string.show_additional)
        }
    }

    fun save(view: View) {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION)
        intent.type = ContactsContract.RawContacts.CONTENT_TYPE

        val name = binding.textName.text.toString()
        val phone = binding.textPhone.text.toString()
        val email = binding.textEmail.text.toString()
        val address = binding.textAddress.text.toString()
        val company = binding.textCompany.text.toString()
        val jobTitle = binding.textJob.text.toString()
        val website = binding.textWebsite.text.toString()
        val im = binding.textIM.text.toString()

        if (name.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.NAME, name)
        }
        if (phone.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone)
        }
        if (email.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email)
        }
        if (address.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address)
        }
        if (jobTitle.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle)
        }
        if (company.isEmpty()) {
            intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company)
        }
        val contactData = ArrayList<ContentValues>()
        if (website.isEmpty()) {
            val websiteRow = ContentValues()
            websiteRow.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
            )
            websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website)
            contactData.add(websiteRow)
        }
        if (im.isEmpty()) {
            val imRow = ContentValues()
            imRow.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE
            )
            imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im)
            contactData.add(imRow)
        }
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData)
        startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
    }

    fun cancel(view: View) = finish()

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            CONTACTS_MANAGER_REQUEST_CODE -> {
                setResult(resultCode, Intent())
                finish()
            }
        }
    }
}