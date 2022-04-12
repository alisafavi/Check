package aliSafavi.check.check_list

import aliSafavi.check.EventObserver
import aliSafavi.check.R
import aliSafavi.check.bank.BankListFragment
import aliSafavi.check.check.CheckFragment
import aliSafavi.check.check.Mode
import aliSafavi.check.databinding.FragmentCheckListBinding
import aliSafavi.check.utils.setupSnackbar
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.MenuItem.SHOW_AS_ACTION_ALWAYS
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.h6ah4i.android.example.swipe_list_menu.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckListFragment : Fragment() {

    private val viewModel : CheckListViewModel by viewModels()
    private lateinit var binding : FragmentCheckListBinding
    private lateinit var checkList : SwipeRecyclerView
    private var lastCheck =0L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCheckListBinding.inflate(inflater, container, false)

        checkList = binding.checkList
        binding.btnNewCheck.setOnClickListener {
            val bundle = bundleOf("lastCheck" to lastCheck)
            it.findNavController().navigate(R.id.action_checkListFragment_to_checkFragment,bundle)
        }
        setHasOptionsMenu(true)

        setupList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Intent(context,BankListFragment::class.java)
        setupSnakbar()
    }

    private fun setupList() {
        val adapter = CheckListAdapter(OnCheckItemClickListener {
            val bundle = bundleOf("checkId" to it,"mode" to Mode.VIEW)
//            navigateToCheckFragment(it,Mode.VIEW)
            CheckFragment.newInstance(bundle)
                .show(childFragmentManager,"dd")
        })

        viewModel.checks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if (!it.isEmpty()){
                lastCheck = it.size.toLong()
            }
        })

        val swipeMenuCreator = object : SwipeMenuCreator {
            override fun onCreateMenu(leftMenu: SwipeMenu?, rightMenu: SwipeMenu?, position: Int) {
                val width = 150
                val height = ViewGroup.LayoutParams.MATCH_PARENT

                val passItem = SwipeMenuItem(context)
                    .setWidth(width)
                    .setHeight(height)
                    .setImage(R.drawable.ic_price_check)
                    .setVerticalMargin(resources.getDimension(R.dimen.text_input_layout_margin_vertical).toInt())
                    .setRightMargin(resources.getDimension(R.dimen.text_input_layout_margin_horizontal).toInt())
                rightMenu?.addMenuItem(passItem)
                val deleteItem = SwipeMenuItem(context).apply {
                    this.width=width
                    this.height=height
                    setImage(R.drawable.ic_delete)
                    setVerticalMargin(resources.getDimension(R.dimen.text_input_layout_margin_vertical).toInt())
                    setLeftMargin(resources.getDimension(R.dimen.text_input_layout_margin_horizontal).toInt())
                }
                leftMenu?.addMenuItem(deleteItem)

                val editItem = SwipeMenuItem(context).apply {
                    this.width=width
                    this.height=height
                    setImage(R.drawable.ic_edit)
                    setVerticalMargin(resources.getDimension(R.dimen.text_input_layout_margin_vertical).toInt())
                    setLeftMargin(resources.getDimension(R.dimen.text_input_layout_margin_horizontal).toInt())
                }
                leftMenu?.addMenuItem(editItem)
            }
        }
        val mMenuItemClickListener = object : OnItemMenuClickListener {
            override fun onItemClick(menuBridge: SwipeMenuBridge?, adapterPosition: Int) {
                menuBridge!!.closeMenu()

                val direction = menuBridge.direction
                val menuPosition = menuBridge.position
                val checkId=adapter.getCheckId(adapterPosition)

                if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                    if (menuPosition==0){
                        viewModel.passCheck(checkId)
                        adapter.notifyDataSetChanged()
                    }
                } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                    when(menuPosition){
                        0-> {
                            deleteCheck(checkId)
                            adapter.notifyItemRemoved(adapterPosition)
                        }
                        1 ->navigateToCheckFragment(checkId,Mode.EDIT)
                    }
                }
            }
        }

        checkList.apply {
            setSwipeMenuCreator(swipeMenuCreator)
            setOnItemMenuClickListener(mMenuItemClickListener)
            layoutManager=LinearLayoutManager(context, RecyclerView.VERTICAL,false)
            this.adapter=adapter
        }
    }

    private fun navigateToCheckFragment(checkId: Long,mode:Mode) {
        val bundel = bundleOf("checkId" to checkId,"mode" to mode)
        findNavController().navigate(R.id.action_checkListFragment_to_checkFragment,bundel)
    }

    private fun deleteCheck(checkId: Long) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.warning)
            .setIcon(android.R.drawable.stat_sys_warning)
            .setMessage(R.string.are_you_sure_to_delete)
            .setNegativeButton(resources.getString(R.string.cancel)){dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                viewModel.deleteCheck(checkId)
                dialog.dismiss()
            }
            .show()
    }


    private fun setupSnakbar() {
        viewModel.checkUpdatedEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                view?.setupSnackbar(it)
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.all->viewModel.filter(Sort.ALL)
            R.id.paid->viewModel.filter(Sort.PAID)
            R.id.unPaid->viewModel.filter(Sort.UN_PAID)
        }
        return false
    }
}
