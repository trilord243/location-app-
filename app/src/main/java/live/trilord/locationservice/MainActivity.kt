package live.trilord.locationservice

import android.content.Context
import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import live.trilord.locationservice.ui.theme.LocationServiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel:LocationViewModel = viewModel()
            LocationServiceTheme {

                    MyApp(viewModel)


            }
        }
    }
}



@Composable
fun MyApp(viewModel:LocationViewModel){
    val context=LocalContext.current
    val locationUtils = LocationUtils(context)

    LocationDisplay(locationUtils = locationUtils, context = context,viewModel)
}

@Composable
fun LocationDisplay(locationUtils: LocationUtils,context: Context, viewModel:LocationViewModel){
    val location = viewModel.location.value
    
    val requestPermissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true   &&  permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true  ){
                //TENGO ACCESO
            }else{
                //tener acceso

                val rationalRequired=ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) ||ActivityCompat.shouldShowRequestPermissionRationale(
                    context ,
                    Manifest.permission.ACCESS_FINE_LOCATION)


                if(rationalRequired){
                    Toast.makeText(context,"Location permision is required for this feature",Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(context,"Location permision is required for this feature",Toast.LENGTH_LONG).show()
                }
            }




    })





    Column(modifier = Modifier.fillMaxSize(), 
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        Text("Location not available")
        if(location!=null){
            Text("Location: ${location.latitude},${location.longitude}")
        }else{
            Text("Location not available")
        }
        Button(onClick = {
            if(locationUtils.hasLocationPermission(context)){
                //Permiso tenido y
                locationUtils.requestLocationUpdates(viewModel)

            }else{
                //Pedir permiso
                requestPermissionLauncher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION

                ))
            }

        }) {
            Text(text = "Get location")
            
        }

    }
}