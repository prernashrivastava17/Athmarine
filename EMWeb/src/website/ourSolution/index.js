import React from "react";
import "./index.scss";
import Header from "../header";
import Footer from "./../footer";

function OurSolutions() {
  return (
    <>
      <Header />
      <main>
        <section className="section_our_solutions">
          <h1>Our Solutions</h1>
          <div className="main_wrapper">
            <div className="wrapper_container">
              <p className="primary_header">
                This platform integrates and exploits many of the latest and
                forward-looking technologies like artificial intelligence (AI)
                and Internet of Things (IoT) to provide an extremely user
                friendly interface to fulfill its purpose.
              </p>
              <p className="secondary_header">
                For customers – vessel owners and vessel managers:
              </p>
              <ul className="listing">
                <li>
                  Reach the right service provider for vessels requirements
                </li>
                <li>
                  Ready comparison between proposals from all eligible service
                  providers
                </li>
                <li>
                  Proposal comparisons based not only on quoted costs but also on
                  competence of the proposed engineer
                </li>
                <li>Enhanced possibility of a first-time fix</li>
                <li>
                  Direct savings of around 25% on budgeted maintenance costs
                </li>
                <li>One stop solution for all your service needs</li>
              </ul>
            </div>

            <div className="wrapper_container">
              <p className="secondary_header">For our partners:</p>
              <ul className="listing">
                <li>Exponentially increase your business profitability</li>
                <li>
                  Expose your business to all vessel owners and managers
                  worldwide
                </li>
                <li>
                  Engage in a fair bidding process for every job of interest
                </li>
                <li>
                  Exercise differential pricing methodology based on demand
                </li>
                <li>Exponentially raise your engineer utilization rate</li>
                <li>
                  Upload service report and raise invoices with a click of a
                  button
                </li>
                <li>Eliminate disputes raised on invoices</li>
                <li>Improvise cash flow by getting paid on time</li>
              </ul>
            </div>
            <div className="wrapper_container">
              <p className="primary_header">
                Feedbacks are sought after each completion of job to ensure a
                continuous process of improvement. This ensures seamless
                process, from raising a service request to delivery of a
                successful service at its fair value.
              </p>
            </div>
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
}

export default OurSolutions;
