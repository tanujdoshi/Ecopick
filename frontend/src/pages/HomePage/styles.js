import 
  {DashboardImage}
 from '../../assets/images/index';

export const styles = {
    backgroundStyle: {
        backgroundImage: `url(${DashboardImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        textAlign: 'center',
        color: '#0000A0', 
      },
      headingStyle: {
        fontWeight: 'bold',
        fontSize: '75px', 
        backgroundColor: '#000', 
        color: '#fff', 
        padding: '10px 20px', 
        borderRadius: '5px', 
        border: 'none'  
      },
      textStyle: {
        fontWeight: 'bold',
        fontSize: '25px', 
        backgroundColor: '#000', 
        color: '#fff', 
        padding: '10px 20px', 
        borderRadius: '5px', 
        border: 'none'  
      }

}

