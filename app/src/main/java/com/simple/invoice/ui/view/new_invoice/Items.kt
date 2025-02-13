package com.simple.invoice.ui.view.new_invoice

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.simple.invoice.R
import com.simple.invoice.data.module.InvoiceItem
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen.cardElevation
import com.simple.invoice.ui.theme.Dimen.dimen7
import com.simple.invoice.ui.theme.Dimen.dimen70
import com.simple.invoice.ui.theme.Dimen.txt12
import com.simple.invoice.ui.theme.Dimen.txt13
import com.simple.invoice.utils.Constants

@Composable
fun Items(
    modifier: Modifier,
    list: MutableList<InvoiceItem>,
){
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = list.size
        ){ index ->
            val invoiceDto = list[index]
            val itemTotalAmount = Constants.calculateItemTotalAmount(invoiceDto.qty, invoiceDto.unitPrice.toDouble())

            ElevatedCard(
                modifier = Modifier
                    .padding(vertical = dimen7)
                    .fillMaxWidth()
                    .height(dimen70),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(cardElevation),
                shape = RoundedCornerShape(dimen7),
                onClick = {
                }
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimen7)
                ) {
                    val (refItem, refQty, refUnitPrice, refTotalAmount) = createRefs()

                    Text(
                        modifier = Modifier
                            .constrainAs(refItem){
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            },
                        text = invoiceDto.item,
                        style = TextStyle(
                            fontSize = txt13,
                            fontWeight = FontWeight.SemiBold,
                            fontStyle = FontStyle.Normal,
                            color = Black
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(refQty){
                                start.linkTo(parent.start)
                                top.linkTo(refItem.bottom, dimen7)
                            },
                        text = "${stringResource(R.string.qty)}: ${invoiceDto.qty}",
                        style = TextStyle(
                            fontSize = txt12,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            color = Black
                        )
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(refUnitPrice){
                                top.linkTo(refItem.bottom, dimen7)
                                end.linkTo(parent.end)
                            },
                        text = "${stringResource(R.string.price)}: ${stringResource(R.string.ruppe_symbol)}${invoiceDto.unitPrice}",
                        style = TextStyle(
                            fontSize = txt12,
                            fontWeight = FontWeight.Normal,
                            fontStyle = FontStyle.Normal,
                            color = Black
                        )
                    )

                    Text(
                        modifier = Modifier
                            .constrainAs(refTotalAmount){
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            },
                        text = "${stringResource(R.string.ruppe_symbol)}$itemTotalAmount",
                        style = TextStyle(
                            fontSize = txt13,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            color = Black
                        )
                    )
                }
            }

        }
    }
}