package com.app.pindout;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Terms extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Terms and condition");
        TextView v=(TextView)findViewById(R.id.term);
        TextView v1=(TextView)findViewById(R.id.term1);
        v.setText("\n1.Your Acceptance of the Agreement for using Pindout’s services\n\n" +
        "A. To use the services provided by Pindout, you must agree to the Terms and Conditions stated below. When you access or use any of services, you agree to be bound these Terms and Conditions. \n" +
        "B. The name ‘Pindout’ has been used interchangeable with ‘Pindout.com’.\n" +
        "C. In case you object or do not accept to any of the Terms stated below, you may not use the Services offered by Pindout. \n" +
        "\n2.Terms of Services by Pindout\n\n" +
        "A. Pindout.com is evolving and is constantly transforming so as to provide the best services and user experience. It is hence stated that the availability of the Services provided by Pindout and Terms may vary from time to time without prior notices. \n" +
        "B. You must acknowledge the fact that Pindout retains the authority to disable or remove  access to your account and prevent you from accessing the Services provided by Pindout , your account details or any data or other content which is contained in your account. \n" +
        "C. By registering with Pindout, you agree to receive certain communications in connection with the site. For example, you might receive compliments or offers from our business partners and other users. You may also receive our weekly e-mail newsletter about happenings in your neighbourhood. Your mobile number will recieve an activation code which you should be using for activating your account. \n" +
        "D. You must acknowledge that Pindout may not currently have set a restriction on the number of transmissions you may send or receive through the Services, such restriction may be set by Pindout.com at any time, at Pindout.com’s discretion. \n" +
        "E. You agree that the registration information provided by you to Pindout.com will always be accurate, correct and up to date \n" +
        "F. You agree that you are solely responsible for (and that Pindout has no responsibility to you or to any third party for) any breach of your obligations under the Terms and for the consequences (including any loss or damage which Pindout.com may suffer) of any such breach. \n" +
        "G. You agree to the use of your data in accordance with Pindout’s Privacy Policy. \n" +
        "H. While using the services provided by Pindout, you agree not to: \n" +
        "a.      Involve or participate in unlawful practices/conduct \n" +
                        "b.      Use the service in any way that is harmful to Pindout  or its advertisers or any customer \n" +
                        "c.      Use the service in any unauthorized means to modify or reroute, or attempt to modify or reroute \n" +
                        "d.      Damage, disable, overburden, or impair the service (or the network(s) connected to the service) or interfere with anyone's use and enjoyment of the service \n" +
                        "e.      Use the Site for commercial purposes, except in connection with a Business User Account and as explicitly permitted by Pindout \n" +
                        "f.        Resell or redistribute the service in full or any part of the service without prior written permission from Pindout.com. \n" +
                        "\n3.CONTENTS \n\n" +
                        "A. Reviews, Ratings and Comments:\n" +
                        "->  You own the complete responsibility of the content you submit to the site and any content you add to the site including but not limited to transmissions like your Reviews, Ratings, Recommendations and Comments, photos, videos posted by you.\n" +
                        "->  Pindout does not endorse or accept any of your views or transmissions as representative of Pindout  views. By submitting any public data  to the site, you automatically grant Pindout  an exclusive, worldwide, perpetual, unrestricted, royalty-free license (with the right to sublicense) to use, reproduce, distribute, publicly display, publicly perform, adapt, modify, edit, create derivative works from, incorporate into one or more compilations and reproduce and distribute such compilations, and otherwise exploit such contents.\n" +
                        "->  You agree that all content you post as Review, Rating or Comments, that the content is accurate; that use of the content you supply does not violate the Terms of IP and will not cause injury to any person or entityPindout     reserves the right to mask or unmask your identity in respect of your Reviews, Ratings & Comments posted by you.\n" +
                " ->  Pindout  has the right, but not the obligation to monitor and edit or remove any content posted by you as Review, Rating or Comments in its sole discretion deems inappropriate. Pindout does not take ownership to review all  or any contents posted to site. \n" +
                " ->  You agree that the content submitted by you is legal and doesn’t contain matter which\n" +

                "      i.  is threating, sexually explicit , obscene, indecent, pornographic, profane,  or abusive\n" +

                "      ii. violates any applicable laws, rules, or regulations of the place where the content originates;\n" +

                "      iii. constitutes or contains false or misleading indications of origin or statements of fact;\n" +

                "     iv.  defames, disparages, slanders, or otherwise violates the legal rights of any third party;\n" +

                "      v.  causes injury or harm of any kind to any person or entity;\n" +

                "->  You agree that, if there are any issues or claims due to your posts by way of Reviews, Ratings and Comments, then Pindout has every right to take appropriate legal action against you. Further, you shall indemnify and protect" +

                "Pindout against such claims or damages or any issues, due to your posting of such contents Pindout takes no" +

                "responsibility and assumes no liability for any content posted by you or any third party on Pindout.com.\n" +

                "\n4.Disclaimer on Data and Information\n\n" +
                "A. While we strive to provide accurate information , Pindout does not guarantee or warranties about the validity," +

                "correctness , reliability ,accuracy, quality, stability, completeness  of any information provided on or through the" +
                "site.  Pindout makes no guarantee, nor does Pindout take any responsibility for the Content including its quality," +
                "copyright compliance or legality, or any resulting loss or damage.  Your use of the site is at your own risk.\n" +
                "B. Unless stated otherwise, all pictures and information contained on this website are believed to be in the public" +
                "domain as either promotional materials, publicity photos or press media stock.\n" +
                "C. Please email a Takedown Request (by using the Contact Us on the home page) to the webmaster under the terms" +
                " of The Copyright Act, 1957 if you are the copyright owner of any content on this website and you think the use" +
                " of the above material violates the Copyright Act in any way. Please indicate the exact URL of the webpage in your request. \n");

v1.setText("5.Proprietary Rights\n\n" +
        "A. You acknowledge and agree that Pindout (or Pindout's licensors) own all legal right, title and interest in and to" +
        "the services, including any intellectual property rights which subsist in the services you further acknowledge" +
        "that the services may contain information which is designated confidential by Pindout and that you shall not\n" +
        "B. Nothing in the Terms gives you a right to use any of Pindout’s trade names, trade marks, service marks, logos," +
        "domain names, and other distinctive brand features, .\n" +
        "C. Unless you have been expressly authorized to do so in writing by Pindout, you agree that in using the Services," +
        "you will not use any trade mark, service mark, trade name, logo of any company or organization in a way that is\n" +
        "\n6.Exclusion of Warranties\n\n" +
        "A. Nothing in these terms, including sections 6 and 7, shall exclude or limit Pindout's warranty or liability for" +
        " losses which may not be lawfully excluded or limited by applicable law. Some jurisdictions do not allow the" +
        " exclusion of certain warranties or conditions or the limitation or exclusion of liability for loss or damage caused" +
        " by negligence, breach of contract or breach of implied terms, or incidental or consequential damages." +
        "Accordingly, only the limitations which are lawful in your jurisdiction will apply to you and our liability will be" +
        " limited to the maximum extent permitted by law.\n" +
        "B. You expressly understand and agree that your use of the services is at your sole risk and that the services are" +
        "C. Pindout, its subsidiaries and affiliates, and its licensors do not represent or warrant to you that:\n" +
        "       i. Your use of the services will be uninterrupted, timely, secure or free from error\n" +
        "      ii. Any information obtained by you as a result of your use of the services will be accurate or reliable\n" +
        "     iii. That defects in the operation or functionality of any software provided to you as part of the services will be corrected.\n" +
        "     iv· Any material downloaded or otherwise obtained through the use of the services is done at your own discretion and risk and that you will be solely responsible for any damage to your computer system or  other device or loss of data that results from the download of any such material.\n" +
        "D. Pindout further expressly disclaims all warranties and conditions of any kind, whether express or implied,including, but not limited to the implied warranties and conditions of merchantability, fitness for a particular  purpose and non-infringement.\n" +
        "\n7.Limitation Of Liability\n\n" +
        "A. Subject to overall provision paraphrased above, you expressly acknowledge and agree that Pindout, its subsidiaries and affiliates, and its licensors shall not be liable to you for: Any direct, indirect, incidental,  special consequential or exemplary damages which may be incurred by you, however caused and under  ny theory of liability, this shall include, but not be limited to, any loss of profit (whether incurred directly or indirectly), any loss of goodwill or business reputation, any loss of data suffered, cost of procurement of  substitute goods or services, or other intangible loss; Any loss or damage which may be incurred by you, including but not limited to loss or damage as a result of:\n" +
        "B.  Any reliance placed by you on the completeness, accuracy or existence of any advertising, or as a result of any relationship or transaction between you and any advertiser or sponsor whose advertising appears on the services;\n" +
        "B.  The deletion of, corruption of, or failure to store, any content and other communications data maintained or transmitted by or through your use of the services;\n" +
        "C.  Your failure to provide Pindout with accurate account information;\n" +
        "D.  Your failure to keep your password or account details secure and confidential\n" +
        "E.  The limitations on Pindout's liability to you in paragraph 7 above shall apply whether or not Pindout has been advised of or should have been aware of the possibility of any such losses arising.\n" +
        "\n8.Indemnify\n\n" +
        "A. You agree to hold harmless, defend and indemnify Pindout, its officers, directors, employees, subsidiaries,contractors, suppliers, agents, partners and affiliates, successors and assigns from all liabilities, claims,demands and expenses, including attorney’s fees that arise from your use or misuse of Pindout.com, any  services therein, or for infringement by you or others of intellectual property rights or other right of any third  party.Pindout.com may assume exclusive control of any defense or any matter subject to indemnification by you and you agree to cooperate with us in such event.\n" +
        "\n9.Advertising\n\n" +
        "A. Some of the Services are supported by advertising revenue and may display advertisements and promotions." +
        "These advertisements may be targeted to the content of information stored on the Services, queries made" +
        "through the Services or other information.\n" +
        "B. Pindout reserves right to change the manner, mode and extent of advertising without prior notice.\n" +
        "C. In consideration for Pindout granting you access to and use of the Services, you agree that Pindout may place" +
        "such advertising on the Services.\n");
    }

}
