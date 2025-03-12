import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.redlable.qrcodescanner.R
import com.redlable.qrcodescanner.databinding.FragmentBarcodeHistoryBinding
import com.redlable.qrcodescanner.di.barcodeDatabase
import com.redlable.qrcodescanner.extension.applySystemWindowInsets
import com.redlable.qrcodescanner.extension.showError
import com.redlable.qrcodescanner.feature.common.dialog.DeleteConfirmationDialogFragment
import com.redlable.qrcodescanner.feature.common.view.CustomViewPager
import com.redlable.qrcodescanner.feature.tabs.history.BarcodeHistoryViewPagerAdapter
import com.redlable.qrcodescanner.feature.tabs.history.export.ExportHistoryActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class BarcodeHistoryFragment : Fragment(), DeleteConfirmationDialogFragment.Listener,
    CustomViewPager.DisableViewPager {

    private var _binding: FragmentBarcodeHistoryBinding? = null
    private val binding get() = _binding!!
    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Support edge-to-edge layout
        supportEdgeToEdge()

        // Initialize the tabs and viewpager
        initTabs()

        // Handle menu item click
        handleMenuClicked()
    }

    override fun onDeleteConfirmed() {
        clearHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
        _binding = null
    }

    private fun supportEdgeToEdge() {
        binding.appBarLayout.applySystemWindowInsets(applyTop = true)
    }

    private fun initTabs() {
        // Use the custom ViewPager here
        binding.viewPager.adapter =
            BarcodeHistoryViewPagerAdapter(requireContext(), childFragmentManager)

        // Setup the TabLayout with the ViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun handleMenuClicked() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_export_history -> navigateToExportHistoryScreen()
                R.id.item_clear_history -> showDeleteHistoryConfirmationDialog()
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun navigateToExportHistoryScreen() {
        ExportHistoryActivity.start(requireActivity())
    }

    private fun showDeleteHistoryConfirmationDialog() {
        val dialog =
            DeleteConfirmationDialogFragment.newInstance(R.string.dialog_delete_clear_history_message)
        dialog.show(childFragmentManager, "")
    }

    private fun clearHistory() {
        barcodeDatabase.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { },
                ::showError
            )
            .addTo(disposable)
    }

    // Disable/enable the ViewPager
    override fun disablePager(disable: Boolean) {
        binding.viewPager.setPagingEnabled(!disable)
    }
}
