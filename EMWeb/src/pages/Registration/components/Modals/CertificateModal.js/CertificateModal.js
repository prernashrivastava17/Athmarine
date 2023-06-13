import "./CertificateModal.scss";
import Backdrop from "./../../../../../components/Backdrop/Backdrop";
import removeIcon from "./../../../../../assets/images/cancel.png";

const CertificateModal = (props) => {
  console.log({ props });
  const imagePrefixVal = window.location.href.includes("qa")
    ? "https://athmarine-qa.s3.ap-south-1.amazonaws.com/"
    : "https://transas.s3.ap-south-1.amazonaws.com/";
  const imagePrefix = imagePrefixVal;

  return (
    <Backdrop>
      <div className="certificate_modal">
        <div className="certificate_modal_top">
          <p className="title">Documents</p>
          <label className="btn_upload">
            Upload
            <input
              type="file"
              accept=".png, .jpg, .jpeg, .pdf"
              data-allowed-file-extension="png, jpg, jpeg, pdf"
              onChange={(e) => props.fileUpload(e, props.activeRow)}
              // disabled={navigationResponse != null ? true : false}
            />
          </label>
        </div>
        <div className="certificate_modal_center">
          {props?.data.length === 1 && props?.data[0]["certificates"] === ""
            ? null
            : props?.data?.map((doc, index) => (
                <div className="certificate_modal_item">
                  <div className="certificate_modal_item_name">
                    {doc?.certificates?.split("/")[1]?.split(".")[0]}
                  </div>
                  <div className="certificate_modal_item_func">
                    <div
                      className="certificate_modal_item_view"
                      onClick={() =>
                        window.open(
                          `${imagePrefix}${doc.certificates}`,
                          "_blank"
                        )
                      }
                    >
                      View
                    </div>
                    <div
                      className="certificate_modal_item_delete"
                      onClick={() => props.onRemove(index, props.activeRow)}
                    >
                      <img
                        src={removeIcon}
                        alt="remove"
                        width="22"
                        height="22"
                      />
                    </div>
                  </div>
                </div>
              ))}
        </div>
        <div className="certificate_modal_bottom">
          <div className="btn_okay" onClick={props.clickOk}>
            Okay
          </div>
        </div>
      </div>
    </Backdrop>
  );
};

export default CertificateModal;
