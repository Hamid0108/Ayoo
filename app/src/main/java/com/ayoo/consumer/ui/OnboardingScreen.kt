package com.ayoo.consumer.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ayoo.consumer.R

data class OnboardingPageData(
    val logoImage: Int,
    val mainImage: Int,
    val title: String,
    val description: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pages = listOf(
        OnboardingPageData(
            R.drawable.ayoo_with_icon,
            R.drawable.ayoo_mascot,
            "Welcome",
            "Hello mga ka AYOO ! Welcome to our new food delivery app in Iligan city."
        ),
        OnboardingPageData(
            R.drawable.ayoo_food_delivery,
            R.drawable.ayoo_delivery,
            "AYOO ! Pa deliver na?",
            "Order from the best local restaurants with easy, on-demand delivery."
        ),
        OnboardingPageData(
            R.drawable.ayoo_myfind_and_more,
            R.drawable.map_pic,
            "New Exciting Features",
            "Easily find your type of food craving and you\'ll get delivery in wide range ."
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Skip")
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            OnboardingPageContent(page = pages[pageIndex])
        }

        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Get Started")
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    val navController = rememberNavController()
    OnboardingScreen(navController = navController)
}


@Composable
fun OnboardingPageContent(page: OnboardingPageData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Increased size for the logo image
        Image(
            painter = painterResource(id = page.logoImage),
            contentDescription = "Ayoo Logo",
            modifier = Modifier
                .fillMaxWidth(0.9f) // Occupy 80% of width
                .heightIn(min = 100.dp, max = 150.dp) // Allow it to be taller
        )
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = page.mainImage),
            contentDescription = null,
            modifier = Modifier.height(200.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = page.description,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Preview
@Composable
fun OnboardingPageContentPreview() {
    val page = OnboardingPageData(
        logoImage = R.drawable.ayoo_delivery,
        mainImage = R.drawable.ayoo_mascot,
        title = "Welcome",
        description = "Hello mga ka AYOO ! Welcome to our new food delivery app in Iligan city."
    )
    OnboardingPageContent(page = page)
}