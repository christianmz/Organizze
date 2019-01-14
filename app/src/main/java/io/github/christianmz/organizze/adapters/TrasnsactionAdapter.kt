package io.github.christianmz.organizze.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.github.christianmz.organizze.R
import io.github.christianmz.organizze.commons.EXPENSE
import io.github.christianmz.organizze.commons.inflate
import io.github.christianmz.organizze.models.Transaction
import kotlinx.android.synthetic.main.cardview_transaction.view.*

class TransactionAdapter(private val transactionList: List<Transaction>): RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.cardview_transaction))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(transactionList[position])

    override fun getItemCount()= transactionList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(transaction: Transaction) = with(itemView){

            tv_transaction.text = transaction.value.toString()
            tv_description.text = transaction.description
            tv_category.text = transaction.category

            if (transaction.type == EXPENSE){
                tv_transaction.setTextColor(ContextCompat.getColor(context, R.color.color_primary_expense))
                tv_transaction.text = "-${transaction.value}"
            } else{
                tv_transaction.setTextColor(ContextCompat.getColor(context, R.color.color_primary_income))
            }
        }
    }
}
