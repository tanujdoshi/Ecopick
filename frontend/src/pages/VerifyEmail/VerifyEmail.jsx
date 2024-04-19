import { useEffect } from "react";
import {
    EmailVerification
  } from "../../assets/images/index";
import { useSearchParams, useNavigate } from 'react-router-dom';
import api from '../../api/index';
import Layout from '../../common/Layout/Layout';


function VerifyEmail() { 
    const [searchParams] = useSearchParams();
    const email = searchParams.get('email');
    const code = searchParams.get('code');
    const type = 'VerifyEmail';
    const navigate = useNavigate();

    useEffect(() => {
      const fetchData = async () => {
          if (email && code) {
              try {
                  const data = await api.auth.verifyEmail(email, code, type);
                  if(data){
                    navigate("/login");
                  }
  
              } catch (error) {
                  console.error('Error verifying email:', error);
              }
          }
      };

      fetchData();

  }, [email, code]);


  return (
    <div className="container mt-5">
    <div className="row justify-content-center align-items-center">
      <div className="col-md-6">
        <div className="text-center mb-4">
          <img src={EmailVerification} alt="Email Verification" className="img-fluid" style={{ maxWidth: "70%", height: "auto" }}/>
        </div>
        <div className="alert alert-success text-center" role="alert">
          We have sent a mail to your registered email account. Please verify your mail to continue.
        </div>
      </div>
    </div>
  </div>
  );
}

VerifyEmail.getLayout = (page) => {
  return (
    <Layout simpleHeader hideAuth>
      {page}
    </Layout>
  );
};

export default VerifyEmail;
