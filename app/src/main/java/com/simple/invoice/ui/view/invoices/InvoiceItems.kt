package com.simple.invoice.ui.view.invoices

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.simple.invoice.R
import com.simple.invoice.data.db.entity.InvoiceEntity
import com.simple.invoice.ui.theme.Dimen
import com.simple.invoice.ui.theme.Dimen.dimen7
import com.simple.invoice.ui.theme.Grey
import com.simple.invoice.utils.Constants.convertTimeInMillisToDate
import kotlin.math.roundToInt

@Composable
fun InvoiceItems(
    modifier: Modifier,
    list: List<InvoiceEntity>,
    onClick: (InvoiceEntity) -> Unit,
    onDelete: (InvoiceEntity) -> Unit
) {

    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
    ) {
        items(list.size) { index ->
            val invoice = list[index]

            var offsetX by remember { mutableFloatStateOf(0f) }

            Box(
                modifier = Modifier
                    .padding(bottom = dimen7)
                    .fillMaxWidth()
                    .height(Dimen.dimen85)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(-130f, 0f)
                        }
                    }
            ) {
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
                                onDelete(invoice)

                            },
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Item delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                ElevatedCard(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(),0) }
                        .fillMaxSize()
                        .clickable {
                            onClick(invoice)
                        },
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(Dimen.dimen3)
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .padding(Dimen.dimen7)
                            .fillMaxWidth()
                    ) {
                        val (refDate, refCustomerName, refInvoiceNo, refTotalAmount) = createRefs()

                        Text(
                            modifier = Modifier
                                .constrainAs(refDate) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                },
                            text = invoice.date.convertTimeInMillisToDate(),
                            style = TextStyle(
                                fontSize = Dimen.txt11,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(refCustomerName) {
                                    start.linkTo(parent.start)
                                    top.linkTo(refDate.bottom, Dimen.dimen3)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                },
                            text = invoice.name,
                            style = TextStyle(
                                fontSize = Dimen.txt14,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(refInvoiceNo) {
                                    start.linkTo(parent.start)
                                    top.linkTo(refCustomerName.bottom)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                },
                            text = invoice.invoiceNo,
                            style = TextStyle(
                                fontSize = Dimen.txt11,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Normal,
                                color = Grey
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            modifier = Modifier
                                .constrainAs(refTotalAmount) {
                                    top.linkTo(
                                        refInvoiceNo.bottom,
                                        Dimen.dimen7
                                    )
                                    end.linkTo(parent.end)
                                },
                            text = context.getString(R.string.ruppe_symbol) + invoice.totalAmount,
                            style = TextStyle(
                                fontSize = Dimen.txt17,
                                fontStyle = FontStyle.Normal,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }
}