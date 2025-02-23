package com.simple.invoice.ui.view.new_invoice

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.simple.invoice.R
import com.simple.invoice.data.module.InvoiceItem
import com.simple.invoice.ui.theme.Black
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.Dimen.cardElevation
import com.simple.invoice.ui.theme.Dimen.dimen7
import com.simple.invoice.ui.theme.Dimen.txt12
import com.simple.invoice.ui.theme.Dimen.txt13
import com.simple.invoice.utils.Constants
import kotlin.math.roundToInt

@Composable
fun Items(
    modifier: Modifier,
    list: MutableList<InvoiceItem>,
    onDelete: (InvoiceItem) -> Unit
){
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = list.size
        ){ index ->
            val invoiceDto = list[index]
            val itemTotalAmount = Constants.calculateItemTotalAmount(invoiceDto.qty, invoiceDto.unitPrice.toDouble())


            var offsetX by remember { mutableFloatStateOf(0f) }

            Box(
                modifier = Modifier
                    .padding(bottom = dimen7)
                    .fillMaxWidth()
                    .height(Dimen.dimen80)
                    .pointerInput(Unit){
                        detectHorizontalDragGestures { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(-130f, 0f)
                        }
                    }
            ){
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimen.dimen7),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                offsetX = 0f
                                onDelete(invoiceDto)
                            },
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Item delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                ElevatedCard(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(),0) }
                        .fillMaxSize(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(cardElevation),
                    shape = RoundedCornerShape(dimen7)
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
}