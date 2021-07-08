package com.salwa.salwa.homepage.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.salwa.salwa.LoginActivity
import com.salwa.salwa.R
import com.salwa.salwa.databinding.FragmentAccountBinding
import com.salwa.salwa.homepage.ui.account.admin.AdminActivity


class AccountFragment : Fragment() {

    private var binding: FragmentAccountBinding? = null
    private var isAlreadyHaveShop = false
    private var isShopVerified = false
    private var uid: String? = null
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Account"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        uid = FirebaseAuth.getInstance().currentUser?.uid
        checkIsAdminOrNot()
        showUsername()
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    private fun checkIsAdminOrNot() {
        uid?.let {
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(it)
                .get()
                .addOnSuccessListener { role ->
                    isAdmin = "" + role.get("role") == "admin"
                    if(isAdmin) {
                        binding?.shop?.text = "Admin"
                        return@addOnSuccessListener
                    }
                    // SELANJUTNYA JIKA BUKAN ADMIN, CEK APAKAH USER PUNYA SHOP ATAU TIDAK
                    checkIsUserHaveShopOrNot()
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkIsUserHaveShopOrNot() {
        // CEK APAKAH USER SUDAH MEMILIKI SHOP ATAU BELUM
            uid?.let {
                FirebaseFirestore
                    .getInstance()
                    .collection("shop")
                    .document(it)
                    .get()
                    .addOnSuccessListener { doc ->
                        if(doc.exists()) {
                            isAlreadyHaveShop = true
                            binding?.shop?.text = "Shop"
                            isShopVerified = "" + doc.get("status") == "verified"
                        } else {
                            isAlreadyHaveShop = false
                            isShopVerified = false
                        }
                    }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun showUsername() {
        uid?.let {
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(it)
                .get()
                .addOnSuccessListener { name ->
                    binding?.textView8?.text = "Hello, ${name.get("name")}"
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickChangePassword()

        clickShop()

        clickLogout()
    }

    private fun clickChangePassword() {
        binding?.changePassword?.setOnClickListener {
            startActivity(Intent(context, ChangePasswordActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun clickShop() {
        // JIKA SUDAH PUNYA SHOP MAKA MASUK KE HALAMAN SHOP, JIKA BELUM, MASUK KE HALAMAN REGISTER SHOP
        binding?.shop?.setOnClickListener {
            if(isAlreadyHaveShop) {
                if(isShopVerified) {
                    startActivity(Intent(activity, ShopActivity::class.java))
                } else {
                    showDialogReviewed()
                }
            } else if(isAdmin) {
                startActivity(Intent(activity, AdminActivity::class.java))
            }
            else {
                startActivity(Intent(activity, CreateShopActivity::class.java))
            }
        }
    }

    private fun showDialogReviewed() {
        val dialog = context?.let { it1 -> AlertDialog.Builder(it1) }
        dialog?.setTitle("Shop Still Reviewed")
        dialog?.setMessage("Please wait until admin review your shop")
        dialog?.setIcon(R.drawable.ic_baseline_check_circle_24)
        dialog?.setPositiveButton("YES"){ it2,_ ->
           it2.dismiss()
        }
        dialog?.show()
    }

    private fun clickLogout() {
        binding?.logout?.setOnClickListener {
                val dialog = context?.let { it1 -> AlertDialog.Builder(it1) }
                dialog?.setTitle("Logout Confirm")
                dialog?.setMessage("Are you sure want to logout ?")
                dialog?.setIcon(R.drawable.ic_baseline_logout_24)
                dialog?.setPositiveButton("YA"){ it2,_ ->
                    // sign out dari firebase autentikasi
                    FirebaseAuth.getInstance().signOut()
                    // go to login activity
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    it2.dismiss()
                    startActivity(intent)
                    activity?.finish()
                }
                dialog?.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}